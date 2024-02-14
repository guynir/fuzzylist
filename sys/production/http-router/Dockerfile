FROM nginx:latest

COPY default.conf /etc/nginx/conf.d/default.conf
COPY html /usr/share/nginx/html

RUN rm /docker-entrypoint.d/10-listen-on-ipv6-by-default.sh