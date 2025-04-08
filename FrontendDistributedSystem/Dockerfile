FROM node:alpine

WORKDIR /app

COPY package.json package-lock.json ./

RUN npm install 

RUN npm rebuild esbuild

COPY . .

CMD [ "npm","run","dev" ]
