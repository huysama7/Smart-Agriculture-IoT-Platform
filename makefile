


start-all:
	cd springbootmca;./copy.sh
	docker compose -f docker-compose-dev.yml up -d