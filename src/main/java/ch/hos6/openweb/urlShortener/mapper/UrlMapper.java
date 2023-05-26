package ch.hos6.openweb.urlShortener.mapper;

import ch.hos6.openweb.urlShortener.domain.dto.UrlDto;
import ch.hos6.openweb.urlShortener.domain.entity.Url;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * This interface defines the mapper for converting between Url and UrlDto objects.
 * It uses the MapStruct library to automatically generate the implementation at compile time.
 * MapStruct provides the @Mapper annotation to declare this interface as a mapper.
 * The componentModel attribute is set to "spring", so that the mapper can be managed as a Spring bean.
 *
 * @author Toubia Oussama
 */
@Mapper(componentModel = "spring")
public interface UrlMapper {
    /**
     * Converts a Url entity to a UrlDto object.
     * @param url the Url entity
     * @return the corresponding UrlDto object
     */
    UrlDto urlToDto(Url url);
    /**
     * Converts a UrlDto object to a Url entity.
     * @param urlDto the UrlDto object
     * @return the corresponding Url entity
     */
    Url DtoToUrl(UrlDto urlDto);
    /**
     * Converts an Iterable of Url entities to an Iterable of UrlDto objects.
     * @param urls the Iterable of Url entities
     * @return the corresponding Iterable of UrlDto objects
     */
    Iterable<UrlDto> urlsToDtos(Iterable<Url> urls);
    /**
     * Converts an Iterable of UrlDto objects to an Iterable of Url entities.
     * @param urlDtos the Iterable of UrlDto objects
     * @return the corresponding Iterable of Url entities
     */
    Iterable<Url> DtosToUrls(Iterable<UrlDto> urlDtos);
}