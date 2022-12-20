package cloud4webdevs.mythaistar.awsinfra;

import software.amazon.awscdk.Duration;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.services.apigateway.LambdaRestApi;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.rds.DatabaseCluster;
import software.constructs.Construct;

import java.util.Map;
import java.util.Objects;

public class ServerlessStack extends Stack {
    public ServerlessStack(final Construct scope, final String id, final DatabaseCluster dbCluster) {
        super(scope, id, null);

        final var dbClusterSecret = dbCluster.getSecret();
        Objects.requireNonNull(dbClusterSecret, "DB Cluster has no secret!");

        final var healthCheckFunction = Function.Builder.create(this, "HealthCheck")
                .runtime(Runtime.JAVA_11)
                .timeout(Duration.seconds(10))
                .memorySize(256)
                .environment(Map.of("DB_SECRET", dbClusterSecret.getSecretArn()))
                .code(Code.fromAsset("../serverless/target/serverless-1.0.0-SNAPSHOT.jar"))
                .handler("cloud4webdevs.mythaistar.serverless.MyThaiStarHandler")
                .build();

        LambdaRestApi.Builder.create(this, "MyThaiStarApi")
                .handler(healthCheckFunction)
                .build();

        dbClusterSecret.grantRead(healthCheckFunction);
    }
}
