# Instructor Code Review Feedback

**Contact**: Dr. Yiji Zhang (yiji.zhang@northwestern.edu)

**Purpose of This Document**:
The instructor will perform code review with respect to software design, error handling, format and style on the main branch every week starting Week 6 using the letter grade A standards.
The following chapters of the textbook are considered: Chapter 1, 2, 3, 4, 5, 6, 7, 9, and 10. The corresponding lectures are considered, too.


## Week 6 Code Review
I have read every line of production code currently in the main branch. Good work overall! 2 pieces of feedback:
1. Null check: In your Piece construcor, you check for null. So here are a couple of things to consider:
1) indeed, it is generally a good practice to do "defense programming". Your null check is a great example.
2) However, what uncle Bob argues in the book is that instead of doing null check everywhere, especially in a language like Java, we should make sure we don't pass null around, period.
3) If you work on an API or a library that is designed to be used by others, the null check is more important. Or, if you work in a language like C or C++ where pointers are allowed, it is more important to check for null explicitly and thoroughly. 
So, what's the conclusion? In general, for this course project, since you are developing a standalone application in Java, it is less important to do null check. Instead, we should focus on making sure we don't pass null around.
2. Magic number: Your Location class uses Magic Numbers. See the definition and fix here: https://checkstyle.sourceforge.io/checks/coding/magicnumber.html. Later you can set up Checkstyle config to check these automatically. 
