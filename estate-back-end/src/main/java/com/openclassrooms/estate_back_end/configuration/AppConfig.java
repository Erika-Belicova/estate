package com.openclassrooms.estate_back_end.configuration;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.nio.file.Paths;

@Configuration
public class AppConfig implements WebMvcConfigurer {

    private final PictureStorageProperties pictureStorageProperties;

    public AppConfig(PictureStorageProperties pictureStorageProperties) {
        this.pictureStorageProperties = pictureStorageProperties;
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addConverter(new Converter<LocalDateTime, String>() {
            public String convert(MappingContext<LocalDateTime, String> context) {
                return context.getSource() == null ? null : context.getSource().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            }
        });
        return modelMapper;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping("/api/**")
                .allowedOrigins("http://localhost:4200")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("Authorization", "Content-Type")
                .allowCredentials(true);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String uploadDir = Paths.get(pictureStorageProperties.getUploadDir()).toAbsolutePath().toUri().toString();
        registry.addResourceHandler(pictureStorageProperties.getPictureUrlPath() + "/**")
                .addResourceLocations(uploadDir);
    }

}
