# ExampleCheck
Are Code Examples on an Online Q&amp;A Forum Reliable? A Study of API Misuse on Stack Overflow (ICSE 2018) Augmenting Stack Overflow with API Usage Patterns Mined from GitHub (FSE 2018 Demo)


## Summary of ExampleCheck 

Programmers often consult an online Q&amp;A forum such as Stack Overflow to learn new
APIs. This paper presents an empirical study on the prevalence and severity of API
misuse on Stack Overflow. To reduce manual assessment effort, we design ExampleCheck,
an API usage mining framework that extracts patterns from over 380K Java repositories
on GitHub and subsequently reports potential API usage violations in Stack Overflow
posts. We analyze 217,818 Stack Overflow posts using ExampleCheck and find that 31%
may have potential API usage violations that could produce unexpected behavior such
as program crashes and resource leaks. Such API misuse is caused by three main reasons---missing
control constructs, missing or incorrect order of API calls, and incorrect guard conditions.
Even the posts that are accepted as correct answers or upvoted by other programmers
are not necessarily more reliable than other posts in terms of API misuse. This study
result calls for a new approach to augment Stack Overflow with alternative API usage
details that are not typically shown in curated examples.

## Team 
This project is developed by Professor [Miryung Kim](http://web.cs.ucla.edu/~miryung/)'s Software Engineering and Analysis Laboratory at UCLA. 
If you encounter any problems, please open an issue or feel free to contact us:

[Tianyi Zhang](https://https://tianyi-zhang.github.io): PhD student and now an assistant professor at Purdue; tianyi@purdue.edu
  
[Ganesha Upadhyaya](https://www.linkedin.com/in/gupadhyaya/): PhD student and now a research engineer at Harmony

[Anastasia Reinhardt](https://www.linkedin.com/in/anastasiareinhardt/): Undergraduate student and now a graduate student at UW
 
[Hridesh Rajan](https://www.cs.iastate.edu/hridesh): Professor at Iowa State University

[Miryung Kim](http://web.cs.ucla.edu/~miryung/): Professor at UCLA; miryung@cs.ucla.edu

## How to cite 
Please refer to our ICSE'18 paper, [Are code examples on an online Q&A forum reliable?: a study of API misuse on stack overflow](http://web.cs.ucla.edu/~miryung/Publications/icse2018-ExampleCheck.pdf) for more details. 

### Bibtex  
@inproceedings{10.1145/3180155.3180260,
author = {Zhang, Tianyi and Upadhyaya, Ganesha and Reinhardt, Anastasia and Rajan, Hridesh and Kim, Miryung},
title = {Are Code Examples on an Online Q&amp;A Forum Reliable? A Study of API Misuse on Stack Overflow},
year = {2018},
isbn = {9781450356381},
publisher = {Association for Computing Machinery},
address = {New York, NY, USA},
url = {https://doi.org/10.1145/3180155.3180260},
doi = {10.1145/3180155.3180260},
booktitle = {Proceedings of the 40th International Conference on Software Engineering},
pages = {886–896},
numpages = {11},
keywords = {online Q&amp;A forum, API usage pattern, code example assessment},
location = {Gothenburg, Sweden},
series = {ICSE '18}
}
[DOI Link](https://doi.org/10.1145/3180155.3180260)

### Slides
You can find ICSE 2018 presentation slides [here](http://web.cs.ucla.edu/~miryung/Publications/icse2018-ExampleCheck-slides.pdf). 

### Tool Demonstration Video and Tutorial 

You can find our tool demo video from FSE 2018 [here](https://youtu.be/WOnN-wQZsH0). 

You can find ExampleCheck tutorials from our FSE 2019 tool demonstration [paper](http://web.cs.ucla.edu/~miryung/Publications/fse2018demo-examplecheck.pdf) and [slides](http://web.cs.ucla.edu/~miryung/Publications/fse2018demo-examplecheck-slides.pdf).  

