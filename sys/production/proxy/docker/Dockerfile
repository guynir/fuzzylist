FROM nginx:latest

WORKDIR /etc/nginx

COPY sites-available/* sites-available/
COPY nginx.conf .

WORKDIR /etc/nginx/sites-enabled
RUN ln -s /etc/nginx/sites-available/default default && \
    ln -s /etc/nginx/sites-available/www www && \
    rm /etc/nginx/conf.d/*
