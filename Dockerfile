FROM 1science/sbt

ADD . /app
WORKDIR /app
ADD http://nlp.stanford.edu/software/stanford-corenlp-full-2015-04-20.zip .
RUN unzip stanford-corenlp-full-2015-04-20.zip
RUN mv stanford-corenlp-full-2015-04-20 lib
RUN sbt compile

EXPOSE 8081
CMD ["sbt", "run", "-port8081"]
