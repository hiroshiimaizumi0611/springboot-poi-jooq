# --- Build Stage ---
FROM node:slim AS build
WORKDIR /app
COPY . .
RUN npm ci && npm run build

# --- Runtime Stage ---
FROM nginx:alpine
COPY --from=build /app/dist /usr/share/nginx/html
EXPOSE 80
CMD [ "nginx", "-g", "daemon off;" ]