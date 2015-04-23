# Course project for CAP5610 Machine Learning<br/>Traffic Information Voice Command Recognizer

###Team member: Yudong Guang, Huibo Wang
---
###Folder structure

The webpage is in `doc/ML_project_webpage.html`

The report is in `doc/project_report.pdf`

---
####1. Abstract
The system in the project is designed to work as
a module that the outer system can use to transform user’s voice
to a URL that it can use to retrieve the corresponding information
that the user is interested in. In this webpage, we introduced our
motivation of doing this project and our design of the system. 
We have implemented the modules
in the design and have a runnable version. We did an evaluation
on the accuracy of the system and also listed the possible future
work. For the details of implementation, evaluation and future work,
please see our full [project report].
####2. Introduction & Motivation
With the exponentially growth of the natural language
processing technology, more and more softwares provide voice
command recognizer as an import interacted method with the
user. Besides by touching and pressing keyboard, user can
interact with the software by simply telling the software what
they want to do. The main advantage is that it facilitates the
communication between human and software, especially when
user can not use their hands to operate the software. The main
goal of implementing voice command control is to get efficient
way for humans to communicate with computers.

[ITPA]\(Informed Traveler Program and Applications),
which we are currently working on, is a project that would
provide personalized, timely information and advice regarding
the most efficient and cost effective travel paths for consumers,
specifically for the university area around FIU.

The ITPA will provide a smartphone-based interface for
users to acquire, request and manipulate the information
needed. Apart from the common interactions between the user
and APP via physically touching the screen, it is very helpful
to allow users to interact with the APP by voice.

The project implemented the traffic information voice command
recognizer (will be referred to as TCVR in the following
context) for the ITPA APP. Briefly, TCVR takes the users voice
as input, recognize the words in the sentence, and extract
the semantic meaning of the command, which will allow
the APP to send the corresponding request to the backend.
TCVR should be able to recognize the common requests of
users, including but not limited to getting parking information,
getting direction to certain location and sending public transit
request.

In some scenarios, commanding by voice is the most
‘natural’ and intuitive way, which can let users interact with
computer with the minimum or even without learning. For
example, using voice-controlled personal computers for dictation
is an important application for physically disabled or
layers; another application is environmental control, such as
turning on the light, controlling the TV etc. In some other
cases, freeing hands from devices can let people concentrate
more on the task instead of controlling. For example, when
people are driving and using cellphone to search for point of
interest, it is safer and easier to let user speak instead of typing.
Hence, apart from the common interactions between the user
and APP via physically touching the screen, it is very helpful
to allow users to interact with the APP by voice. For example,
the user can speak to the APP to get the parking information
of the nearest parking garage in campus, or the user can speak
to the APP to send a transit request. Allowing interacting with
the APP by voice can prevent the users from typing on the
phone while driving and thus improve the overall safety of
traffic on campus, especially FIU is one of the most populated
university in America.

####3. Architecture
![TCVR Architecture](https://raw.githubusercontent.com/hwang033/tcvr/master/doc/ml_architecture.png "TCVR Architecture")

The above figure shows the architecture of ITPA traffic command
voice recognizer. There are 5 modules in the system:
* voice recognizing tool
* refining filter
* command type classifier
* dependency parser
* URL generator.

The input of this system is voice, and output is the corresponding
URL. ITPA can call the URL to retrieve and display
the requested transit information.

Voice recognizing tool transform the input voice to text
format. In this module a grammar file is needed to define
the questions’ type. This grammar file is generated from a
survey. Different person may have different way to express
the same meaning. So we used a survey to collect how people
asked questions. This module is based on CMU Sphinx speech
recognition package.

Refining filter module is used to refine the text generated
from voice recognizing tool. Bus route, bus stops, garage name
and abbreviation will be replaced by a unified format. The
refined text file will improve the accuracy of classifier.

Command type classifier leverages support vector machine
(SVM). There are 12 types of questions, such as parking
occupancy information, user position and bus location. The
classifier will predict the question type for each refined text
based on the trained SVM model. The model is trained using
the questions from the Grammar file.

Dependency parser provides the grammatical structure of
sentences, for instance, which words are subject or object of
a verb. After the question type is determined, the next step
is extracting the parameters. For example, user may request
the bus location from FIU main campus to engineer center.Dependency parser will extract the engineer center and FIU
main campus and the ”from-to” relationship between them.

The last module is called URL generator. Each question type
has a corresponding URL, and each URL may have some parameters.
The question type is calculated from command type
classifier and the parameters is extracted from the dependency
parser. In this module is trying to map the question type to a
URL and give the parameters.

####4. Implementation, Evaluation and Future work
For the details of implementation, evaluation and future work, please see our full [project report], in which we introduced our design and implementation details for each module.

Also, please don't hesitate to checkout our GitHub [repository] where we have published all our code and documentation.

####5. Conclusion
* Voice command control can facilitate the communication between human and machine
* We have found a way to implement a voice command recognizer for specific domain
* We made our semantic extraction easier by introducing SVM classifier

[project report]:https://github.com/hwang033/tcvr/blob/master/doc/project_report.pdf
[ITPA]:http://government.fiu.edu/_assets/docs/UniversityCityProjectOverview.pdf
[repository]:https://github.com/hwang033/tcvr
