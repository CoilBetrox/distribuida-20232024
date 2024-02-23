package com.distribuida.authors;

import jakarta.ws.rs.core.Application;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;

@OpenAPIDefinition(
        info = @Info(
                title = "Authors API",
                version = "1.0.1",
                contact = @Contact(
                        name = "RoberthTroya",
                        url = "https://exampleurl.com/contact",
                        email = "techsupport@example.com"
                ),
                license = @License(
                        name = "Apache 2.0"
                )
        )
)
public class AuthorsApplication extends Application {
}
