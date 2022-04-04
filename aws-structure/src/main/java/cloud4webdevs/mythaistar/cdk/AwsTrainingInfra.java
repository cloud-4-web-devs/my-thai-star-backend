package cloud4webdevs.mythaistar.cdk;

import software.amazon.awscdk.Duration;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.services.ec2.*;
import software.amazon.awscdk.services.ecr.Repository;
import software.amazon.awscdk.services.ecs.*;
import software.amazon.awscdk.services.elasticloadbalancingv2.*;
import software.amazon.awscdk.services.elasticloadbalancingv2.HealthCheck;
import software.amazon.awscdk.services.elasticloadbalancingv2.Protocol;
import software.constructs.Construct;

import java.util.*;

public class AwsTrainingInfra extends Stack {
    public AwsTrainingInfra(final Construct scope, final String name) {
        super(scope, name);

        final Vpc vpc = Vpc.Builder.create(this, "vpc")
                .vpcName("vpc-awstraining")
                .maxAzs(3)  // Default is all AZs in region
                .build();

        final Repository repository = Repository.Builder.create(this, "ecr")
                .repositoryName("ecr-awstraining")
                .build();

        final Cluster cluster = Cluster.Builder.create(this, "cluster")
                .clusterName("cluster-awstraining")
                .vpc(vpc)
                .build();

        final SecurityGroup securityGroup = SecurityGroup.Builder.create(this, "sg")
                .securityGroupName("securityGroup-awstraining")
                .vpc(vpc)
                .build();
        securityGroup.addIngressRule(Peer.ipv4(vpc.getVpcCidrBlock()), Port.tcp(80), "Application access");
        securityGroup.addIngressRule(Peer.ipv4(vpc.getVpcCidrBlock()), Port.tcp(443), "Access to ecr");

        final ApplicationTargetGroup applicationTargetGroup = ApplicationTargetGroup.Builder.create(this, "tg")
                .targetGroupName("tg-awstraining")
                .port(80)
                .protocol(ApplicationProtocol.HTTP)
                .targetType(TargetType.IP)
                .vpc(vpc)
                .healthCheck(HealthCheck.builder().protocol(Protocol.HTTP).timeout(Duration.seconds(15)).path("/status/health/ping").build())
                .build();

        final ApplicationLoadBalancer applicationLoadBalancer = ApplicationLoadBalancer.Builder.create(this, "alb")
                .vpc(vpc)
                .loadBalancerName("alb-awstraning")
                .internetFacing(true)
                .build();

        ApplicationListener.Builder.create(this, "listener")
                .loadBalancer(applicationLoadBalancer)
                .port(80)
                .defaultTargetGroups(new ArrayList<>(Collections.singletonList(applicationTargetGroup)))
                .build();
    }
}


