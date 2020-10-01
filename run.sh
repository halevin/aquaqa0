#!/bin/bash
# achalevi, 2019/12/07
mvn clean package
if [[ "$?" -ne 0 ]] ; then
  echo 'could not perform mvn clean package for webaqua'; exit $rc
fi
java -jar target/aqua-qa0-OBOPS-2020.10-SNAPSHOT.jar
