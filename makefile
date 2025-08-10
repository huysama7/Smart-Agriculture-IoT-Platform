


start-all:
	cd springbootmca;./copy.sh
	docker compose -f docker-compose-dev.yml up -d

build-auth:
	cd springbootmca; mvn clean package; ./copy.sh
	docker compose -f docker-compose-dev.yml down --remove-orphans
	docker rmi $$(docker images -q authentication-service) --force || true
	docker compose -f docker-compose-dev.yml build --no-cache authentication-service
	docker compose -f docker-compose-dev.yml up -d authentication-service
