#Encode Audio Legacy
Your are involve in a new project called encode audio. As this project starts to be to diffcult to maintain and evolve and you are the expert, you are in charge to refactor it. As a craftman, you decide to start by writing unit tests in order to reach 100% code coverage. But as usual, you have little time to do it : 4 hours max.

The futur of the project is in your hand. Good luck!

# Step 0: first steps 
In order to learn step by step, you start working with a simplified version of the project. 

Goal, verify your environment :

1. build mvn clean compile
2. There's a unit test: checkJUnit(). 
	1. It should fail. 
	2. Fix it.
3. Run code coverage you should have 0%
4. There's a unit test: checkApprovalTestsReporter().
	1. Remove @Ignore annotation
	2. It should open a window corresponding to your default diff tool

##Acceptance tests
At the end of this step:

1. I am able to build the project
2. I can launch a unit test
3. I can see the code coverage
4. Approval Tests is able to launch a diff tool

# Step 2: Coverage by input parameter variation

Instead of the tests in place, take advantage of LegacyApprovals.lockdown() to generate 100% coverage. 

Refactor out duplication between tests by extracting a helper method. It is going to take 3 arguments. Those are the arguments that have to be varied through the parameters passed in the lockdown method.

##Acceptance tests
At the end of this step:

1. There is only one test method
2. All branches (except IOException in CoreUtil)  are covered

# Time keeper
You have 20 minutes!

