codebuild_notification_settings.yml
-----------------------------------

Read by https://github.com/timeoutdigital/platform/tree/master/lambda/codebuild_notifications
to determine where to send notifications for failed Master builds.


publish-buildspec.yml 
---------------------

Run by AWS CodePipeline when a GitHub release is created, status can be viewed at:
https://eu-west-1.console.aws.amazon.com/codesuite/codebuild/511042647617/projects/scala-commons-publish

pr-buildspec.yml
----------------

Run by https://github.com/timeoutdigital/platform/blob/master/cloudformation/pr-codebuild.yml
When a PR is created or updated, status of builds can be viewed at:
https://eu-west-1.console.aws.amazon.com/codesuite/codebuild/511042647617/projects/scala-commons-test-pr


master-buildspec.yml
----------------

Run by https://github.com/timeoutdigital/platform/blob/master/cloudformation/master-codebuild.yml
When a PR is merged, status of builds can be viewed at:
https://eu-west-1.console.aws.amazon.com/codesuite/codebuild/511042647617/projects/scala-commons-test-master
