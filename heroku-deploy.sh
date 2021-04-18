#!/bin/sh

# shellcheck disable=SC2046

REGISTRY_IMAGE=$(heroku config:get REGISTRY_IMAGE -a "$1")
APP_NAME=$(echo "$REGISTRY_IMAGE" | cut -d '/' -f 2)
TAG=$(echo "$REGISTRY_IMAGE" | cut -d '/' -f 3)

docker login --username=_ --password=$(heroku auth:token) registry.heroku.com \
  && docker pull "$REGISTRY_IMAGE" || true \
  && docker build \
    --cache-from "$REGISTRY_IMAGE" \
    --tag "$REGISTRY_IMAGE" \
    --file ./Dockerfile "." \
  && docker push "$REGISTRY_IMAGE" \
  && heroku container:release -a "$APP_NAME" "$TAG"
