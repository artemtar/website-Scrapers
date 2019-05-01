# https://github.com/elastic/logstash-docker
FROM docker.elastic.co/logstash/logstash-oss:6.7.0

USER root
RUN yum -y install vim && yum clean all

RUN mkdir -p /etc/logstash/conf
COPY pipeline/logstash.conf /etc/logstash/conf/logstash.conf
COPY .vimrc /usr/share/logstash/
RUN chown -R logstash:logstash /etc/logstash
#USER logstash
