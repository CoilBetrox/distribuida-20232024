package com.distribuida.app.books.rest;

import com.distribuida.app.books.clients.AuthorRestClient;
import com.distribuida.app.books.db.Book;
import com.distribuida.app.books.dtos.BookDto;
import com.distribuida.app.books.repo.BookRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.transaction.UserTransaction;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;
import java.util.stream.Collectors;

@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
@Transactional
@OpenAPIDefinition(
        tags = {
                @Tag(name = "widget", description = "Widget operations."),
                @Tag(name = "gasket", description = "Operations related to gaskets")
        },
        info = @Info(
                title = "Book API",
                version = "1.0.1",
                contact = @Contact(
                        name = "Roberth Troya",
                        email = "rdtroyar1@uce.edu.ec"),
                license = @License(
                        name = "Apache 2.0"
                )
        )
)
public class BookRest {
    @Inject
    BookRepository repo;

    @Inject
    @RestClient
    AuthorRestClient authorClient;

    @GET
    @Operation( summary = "Busca todos libros",
    description = "no recibe parametros")
    public List<BookDto> findAll() {
       return repo.streamAll()
               .map(book->{
                   var author = authorClient.findById(book.getAuthorId());

                   var dto = BookDto.from(book);

                   String aname = String.format("%s %s",
                           author.getLastName(), author.getFirstName());

                   dto.setAuthorName( aname );

                   return dto;
               })
               .collect(Collectors.toList());
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Busca un libro por ID",
    description = "recibe como parametro Integer")
    public Response findById(@PathParam("id")Integer id) {
        var book = repo.findByIdOptional(id);

        if(book.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(book.get()).build();
    }

    @POST
    @Operation(summary = "Crea un nuevo libro",
    description = "recibe como parametro un objeto libro")
    public Response create(Book obj) {
        obj.setId(null);

        repo.persist(obj);

        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "actualia un libro por ID",
    description = "recibe como parametro Integer para ID y objeto Libro")
    public Response update(@PathParam("id")Integer id, Book obj) {

        Book b = repo.findById(id);

        b.setIsbn(obj.getIsbn());
        b.setTitle(obj.getTitle());
        b.setPrice(obj.getPrice());
        b.setAuthorId(obj.getAuthorId());

        return Response.ok()
                .build();
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Elimina un libro por ID",
    description = "recibe como parametro un Integer para id")
    public Response delete(@PathParam("id")Integer id) {
        repo.deleteById(id);

        return Response.ok()
                .build();
    }
}
