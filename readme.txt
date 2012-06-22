Implementation of the IBcs machine learning algorithm used for proof of concept testing and generating results for publication. I would not suggest using it 'as is' in a production environment without using a more robust DB handler and exploring potential bottlenecks for greater efficiency in use with large data sets.

Main.java: drives the tests and stores values for where the data resides and which tests will be run.

DB.java/Instances.java: is a small DB handler

Evaluation.java: stores the test suite for cross validation and percentage split testing.

IBk.java: is a IBk (modified kNN) implementation

IBcs.java: implementation of the IBcs machine learning algorithm, a modified kNN that sacrifices accuracy for removing the need to select a k value.

ConceptDescription.java: distance comparison functions and instances interface for IBk and IBcs