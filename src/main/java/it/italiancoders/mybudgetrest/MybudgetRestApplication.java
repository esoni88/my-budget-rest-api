package it.italiancoders.mybudgetrest;

import it.italiancoders.mybudgetrest.dao.CategoryDao;
import it.italiancoders.mybudgetrest.dao.RoleRepository;
import it.italiancoders.mybudgetrest.dao.UserDao;
import it.italiancoders.mybudgetrest.model.dto.UserRole;
import it.italiancoders.mybudgetrest.model.entity.CategoryEntity;
import it.italiancoders.mybudgetrest.model.entity.UserEntity;
import it.italiancoders.mybudgetrest.model.entity.UserRoleEntity;
import it.italiancoders.mybudgetrest.service.category.impl.CategoryManagerImpl;
import it.italiancoders.mybudgetrest.service.local.LocaleUtilsMessage;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

@SpringBootApplication
public class MybudgetRestApplication {

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	LocaleUtilsMessage localeUtilsMessage;


	@Autowired
	private JavaMailSender sender;

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@PostConstruct
	void started() {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}

	protected static final Logger  logger = LoggerFactory.getLogger(MybudgetRestApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(MybudgetRestApplication.class, args);
	}

	public void initDefCategories(CategoryDao categoryDao) {
		List<CategoryEntity> categoryEntities = new ArrayList<>();
		categoryEntities.add(
				CategoryEntity.newBuilder()
						.name("Cibo & Bevande")
						.isReadOnly(true)
						.username(CategoryManagerImpl.GLOBAL_USERNAME)
						.description("Cibo & Bevande")
						.build()

		);
		categoryEntities.add(
				CategoryEntity.newBuilder()
						.name("Shopping")
						.isReadOnly(true)
						.username(CategoryManagerImpl.GLOBAL_USERNAME)
						.description("Shopping")
						.build()

		);
		categoryEntities.add(
				CategoryEntity.newBuilder()
						.name("Casa")
						.isReadOnly(true)
						.username(CategoryManagerImpl.GLOBAL_USERNAME)
						.description("Casa")
						.build()

		);
		categoryEntities.add(
				CategoryEntity.newBuilder()
						.name("Trasporti")
						.isReadOnly(true)
						.username(CategoryManagerImpl.GLOBAL_USERNAME)
						.description("Trasporti")
						.build()

		);
		categoryEntities.add(
				CategoryEntity.newBuilder()
						.name("Veicoli")
						.isReadOnly(true)
						.username(CategoryManagerImpl.GLOBAL_USERNAME)
						.description("Veicoli")
						.build()

		);
		categoryEntities.add(
				CategoryEntity.newBuilder()
						.name("Intrattenimento")
						.isReadOnly(true)
						.username(CategoryManagerImpl.GLOBAL_USERNAME)
						.description("Intrattenimento")
						.build()

		);
		categoryDao.saveAll(categoryEntities);
	}

	@Bean
	public CommandLineRunner loadData(UserDao userRepository, RoleRepository authorityRepository, CategoryDao categoryDao) {
		return (args) -> {

			UserEntity user=userRepository.findByUsernameIgnoreCase("admin");

			if(user == null){

				/**
				 * Inizializzo i dati del mio test
				 */

				List<UserRoleEntity> roles =  Arrays.asList(UserRole.values()).stream().map((ur) -> {
					UserRoleEntity role = UserRoleEntity.newBuilder().id(ur.getValue()).build();
					return authorityRepository.save(role);
				}).collect(Collectors.toList());

				user = UserEntity.newBuilder()
						.username("admin")
						.email("admin@gmail.com")
						.password(passwordEncoder.encode("admin"))
						.status(UserEntity.UserStatusEnum.Active)
						.roles(roles)
						.build();
				userRepository.save(user);
				UserEntity user2= userRepository.findByUsernameIgnoreCase(user.getUsername());
				logger.info("debug [{}] " , user2);
				this.initDefCategories(categoryDao);

			}
		};
	}

}
