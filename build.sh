# Gatekeeper build.sh
cd frontend;
rm -rf node_modules/;
rm -rf dist/
npm install;
npm run build;
rm -rf ../backend/src/main/resources/static/*;
cp -rf dist/* ../backend/src/main/resources/static;
cd ../backend/;
./mvnw clean package;