#Encode Audio Legacy
You're involved in a new project called encode audio. As this project starts to be too diffcult to maintain and evolve you become the expert and you're in charge to save it through refactoring. As a craftman, you decide to start by writing unit tests in order to reach 100% code coverage. But as usual, you have little time to do it : a few hours max.

The future of the project is in your hands. Good luck!

# Requirements
* JDK 7.x  
* Eclipse + EclEmma + m2e (or equivalent)
* Maven 3.x
* Diff-tool : for instance DiffMerge, WinMerge or Meld 
* Git
* Internet/Wifi (for downloading maven dependencies)
  * Please do mvn clean compile beforehand

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


