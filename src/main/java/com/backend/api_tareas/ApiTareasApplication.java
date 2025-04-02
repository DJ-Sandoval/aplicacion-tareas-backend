package com.backend.api_tareas;

import com.backend.api_tareas.persistence.entity.PermissionEntity;
import com.backend.api_tareas.persistence.entity.RoleEntity;
import com.backend.api_tareas.persistence.entity.RoleEnum;
import com.backend.api_tareas.persistence.entity.UserEntity;
import com.backend.api_tareas.persistence.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Set;

@SpringBootApplication
public class ApiTareasApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiTareasApplication.class, args);
	}
}
