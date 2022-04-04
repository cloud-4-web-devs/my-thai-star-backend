package cloud4webdevs.mythaistar.cdk;

import software.amazon.awscdk.Stack;
import software.amazon.awscdk.services.ec2.Vpc;
import software.constructs.Construct;

public class AwsTrainingInfra extends Stack {
    public AwsTrainingInfra(final Construct scope, final String name) {
        super(scope, name);

        Vpc.Builder.create(this, "vpc")
                .vpcName("vpc-awstraining")
                .maxAzs(3)  // Default is all AZs in region
                .build();
    }
}


