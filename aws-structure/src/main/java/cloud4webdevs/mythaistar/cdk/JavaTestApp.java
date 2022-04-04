package cloud4webdevs.mythaistar.cdk;

import software.amazon.awscdk.App;

public class JavaTestApp {
    public static void main(final String[] args) {
        App app = new App();

        new AwsTrainingInfra(app, "infra");

        app.synth();

    }
}

