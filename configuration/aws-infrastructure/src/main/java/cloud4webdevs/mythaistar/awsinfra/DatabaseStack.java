package cloud4webdevs.mythaistar.awsinfra;

import org.jetbrains.annotations.Nullable;
import software.amazon.awscdk.Aspects;
import software.amazon.awscdk.SecretValue;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.services.ec2.*;
import software.amazon.awscdk.services.rds.*;
import software.amazon.awscdk.services.rds.InstanceProps;
import software.constructs.Construct;

import java.util.List;

public class DatabaseStack extends Stack {
    private final DatabaseCluster dbCluster;

    public DatabaseStack(@Nullable Construct scope, @Nullable String id) {
        super(scope, id, null);

        final var vpc = Vpc.Builder.create(this, "vpc-my-thai-star-db")
                .vpcName("vpc-my-thai-star-db")
                .ipAddresses(IpAddresses.cidr("10.0.0.0/16"))
                .subnetConfiguration(List.of(SubnetConfiguration.builder()
                        .name("ingress").subnetType(SubnetType.PUBLIC).build()))
                .natGateways(0)
                .build();

        final var securityGroup = SecurityGroup.Builder.create(this, "security-group-my-thai-star-db")
                .securityGroupName("security-group-my-thai-star-db")
                .vpc(vpc)
                .allowAllOutbound(true)
                .build();

        securityGroup.addIngressRule(Peer.anyIpv4(), Port.tcp(5432), "Access to DB");

        dbCluster = DatabaseCluster.Builder.create(this, "MyThaiStarDbCluster")
                .engine(DatabaseClusterEngine.auroraPostgres(AuroraPostgresClusterEngineProps.builder()
                        .version(AuroraPostgresEngineVersion.VER_13_6)
                        .build()))
                .instances(1)
                .instanceProps(InstanceProps.builder()
                        .vpc(vpc)
                        .instanceType(new InstanceType("serverless"))
                        .publiclyAccessible(true)
                        .securityGroups(List.of(securityGroup))
                        .vpcSubnets(SubnetSelection.builder().subnetType(SubnetType.PUBLIC).build())
                        .build())
                .port(5432)
//                .credentials(Credentials.fromPassword("postgres", SecretValue.unsafePlainText("admin5432")))
                .build();

        Aspects.of(dbCluster).add(node -> {
            if (node instanceof CfnDBCluster) {
                ((CfnDBCluster) node).setServerlessV2ScalingConfiguration(CfnDBCluster.ServerlessV2ScalingConfigurationProperty.builder()
                                .minCapacity(0.5)
                                .maxCapacity(1)
                        .build());
            }
        });
    }

    public DatabaseCluster getDbCluster() {
        return dbCluster;
    }
}
