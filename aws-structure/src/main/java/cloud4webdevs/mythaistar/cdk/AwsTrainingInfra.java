package cloud4webdevs.mythaistar.cdk;

import software.amazon.awscdk.Duration;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.services.ec2.*;
import software.amazon.awscdk.services.ecr.Repository;
import software.amazon.awscdk.services.ecs.*;
import software.amazon.awscdk.services.elasticloadbalancingv2.*;
import software.amazon.awscdk.services.elasticloadbalancingv2.HealthCheck;
import software.amazon.awscdk.services.elasticloadbalancingv2.Protocol;
import software.amazon.awscdk.services.logs.LogGroup;
import software.constructs.Construct;

import java.util.*;

public class AwsTrainingInfra extends Stack {
    public AwsTrainingInfra(final Construct scope, final String name) {
        super(scope, name);

        final Repository repository = Repository.Builder.create(this, "ecr")
                .repositoryName("ecr-awstraining")
                .build();

        final Vpc vpc = Vpc.Builder.create(this, "vpc")
                .vpcName("vpc-awstraining")
                .maxAzs(3)  // Default is all AZs in region
                .gatewayEndpoints(Map.of(
                        "S3", GatewayVpcEndpointOptions.builder()
                                .service(GatewayVpcEndpointAwsService.S3)
                                .build()))
                .build();

        final Cluster cluster = Cluster.Builder.create(this, "cluster")
                .clusterName("cluster-awstraining")
                .vpc(vpc)
                .build();

        final SecurityGroup securityGroup = SecurityGroup.Builder.create(this, "sg")
                .securityGroupName("securityGroup-awstraining")
                .vpc(vpc)
                .build();
        securityGroup.addIngressRule(Peer.ipv4(vpc.getVpcCidrBlock()), Port.tcp(443), "Access to ecr and endpoints");
        securityGroup.addIngressRule(Peer.ipv4(vpc.getVpcCidrBlock()), Port.tcp(80), "Access to application");

        final ApplicationTargetGroup applicationHttpsTargetGroup = ApplicationTargetGroup.Builder.create(this, "tgHttps")
                .targetGroupName("tg-awstraining")
                .port(80)
                .protocol(ApplicationProtocol.HTTP)
                .targetType(TargetType.IP)
                .vpc(vpc)
                .healthCheck(HealthCheck.builder().protocol(Protocol.HTTP).timeout(Duration.seconds(120)).interval(Duration.seconds(240)).path("/status/health/ping").build())
                .build();

        final ApplicationLoadBalancer applicationLoadBalancer = ApplicationLoadBalancer.Builder.create(this, "alb")
                .vpc(vpc)
                .loadBalancerName("alb-awstraning")
                .internetFacing(true)
                .build();

        ApplicationListener.Builder.create(this, "listenerHttps")
                .loadBalancer(applicationLoadBalancer)
                .port(443)
                .certificates(new ArrayList<>(Collections.singletonList(ListenerCertificate.fromArn("arn:aws:acm:eu-central-1:931105827624:certificate/c9853bf6-9fe0-4ac7-8258-2643bb811ea0"))))
                .defaultTargetGroups(new ArrayList<>(Collections.singletonList(applicationHttpsTargetGroup)))
                .build();

        final LogGroup logGroup = LogGroup.Builder.create(this, "logGroup")
                .logGroupName("awstraining-logs")
                .build();

        final AwsLogDriverProps awsLogDriverProps = AwsLogDriverProps.builder()
                .logGroup(logGroup)
                .streamPrefix("cdk")
                .build();

        final FargateTaskDefinition taskDefinition = FargateTaskDefinition.Builder.create(this, "taskDefinition")
                .cpu(256)
                .memoryLimitMiB(512)
                .build();

        taskDefinition.addContainer("taskDefinitionContainer", ContainerDefinitionOptions.builder()
                .image(ContainerImage.fromEcrRepository(repository))
                .portMappings(List.of(PortMapping.builder().containerPort(80).build()))
                .logging(LogDrivers.awsLogs(awsLogDriverProps))
                .build());

        FargateService.Builder.create(this, "service")
                .taskDefinition(taskDefinition)
                .securityGroups(new ArrayList<>(Collections.singletonList(securityGroup)))
                .serviceName("awstraining-service")
                .cluster(cluster)
                .build()
                .attachToApplicationTargetGroup(applicationHttpsTargetGroup);

        // Endpoints
        final String endpointPrefix = "com.amazonaws.eu-central-1.";
        final String[] endpoints = {"ecr.api", "ecr.dkr", "logs", "ssm"};
        for (final String id : endpoints){
            InterfaceVpcEndpoint.Builder.create(this, id)
                    .vpc(vpc)
                    .service(new InterfaceVpcEndpointService(endpointPrefix + id))
                    .securityGroups(new ArrayList<>(Collections.singletonList(securityGroup)))
                    .subnets(SubnetSelection.builder()
                            .subnets(vpc.getPrivateSubnets())
                            .build())
                    .build();
        }
    }
}