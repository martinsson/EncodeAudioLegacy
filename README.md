#Encode Audio Legacy

##Resources
- [Workshop Slides](http://fr.slideshare.net/sanlaville/20140530-itakegolden-master)
- [Forum](https://groups.google.com/forum/#!forum/temporary-testing)

###Libraries
- [ApprovalTests](http://approvaltests.sourceforge.net/)
- [Moco](http://github.com/dreamhead/moco)
- [XStream](http://xstream.codehaus.org/)

###Blog
- [Golden Master and legacy - a few insights ](http://martinsson-johan.blogspot.fr/2014/01/golden-master-and-legacy-few-insights.html)
- [Golden Master and test data](http://martinsson-johan.blogspot.fr/2014/05/golden-master-and-test-data.html)
- [Refactorer legacy même pas peur ](http://martinsson-johan.blogspot.fr/2014/05/refactorer-legacy-meme-pas-peur.html)

###Other useful stuff
- [Working Effectively with Legacy Code, M. Feathers](http://www.amazon.com/Working-Effectively-Legacy-Robert-Martin-ebook/dp/B005OYHF0A) in particular the part on "seams"
- [Specification-by-Example crossed with Approval Testing](https://github.com/pearlfish/pearlfish-java)


## Challenge
You're involved in a new project called encode audio. As this project starts to be too diffcult to maintain and evolve you become the expert and you're in charge to save it through refactoring. As a craftman, you decide to start by writing unit tests in order to reach 100% code coverage. But as usual, you have little time to do it : a few hours max.

The future of the project is in your hands. Good luck!

[Instructions step by step](http://martinsson.github.io/EncodeAudioLegacy)

 
### Requirements
* JDK 7.x  
* Eclipse + EclEmma + m2e (or equivalent)
* Maven 3.x
* Diff-tool : for instance DiffMerge, WinMerge or Meld 
* Git
* Internet/Wifi (for downloading maven dependencies)
  * Please do mvn clean compile beforehand

### Step 0: first steps 
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

###Acceptance tests
At the end of this step:

1. I am able to build the project
2. I can launch a unit test
3. I can see the code coverage
4. Approval Tests is able to launch a diff tool


