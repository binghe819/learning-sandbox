FROM node:10

# Create app directory
WORKDIR /usr/src/app

# package.json을 임시 도커 컨테이너 안으로 복사.
COPY package*.json ./

# 추가적으로 필요한 파일들을 다운로드한다. 이때 npm을 통해 의존성 파일을 설치해준다.
RUN npm install
# If you are building your code for production
# RUN npm ci --only=production

# server.js 포함 모든 파일을 임시 컨테이너로 옮기기 위해서 COPY를 이용해준다.
COPY ./ ./

# 8080 포트를 열어준다.
EXPOSE 8080

# node.js를 실행해준다.
CMD [ "node", "server.js" ]
