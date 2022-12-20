package cloud4webdevs.mythaistar.awsinfra;

import software.amazon.awscdk.App;

public class AppAwsInfrastructure {
    public static void main(final String[] args) {
        final var app = new App();
        final var databaseStack = new DatabaseStack(app, "MyThaiStarDatabaseStack");
        new ServerlessStack(app, "MyThaiStarServerlessStack", databaseStack.getDbCluster());
        app.synth();
    }
}

