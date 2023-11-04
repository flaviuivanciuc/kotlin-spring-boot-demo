package io.reeserve.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.core.convert.converter.Converter
import org.springframework.lang.NonNull
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter
import org.springframework.stereotype.Component
import java.util.stream.Collectors
import java.util.stream.Stream


@Component
class JwtAuthConverter : Converter<Jwt, AbstractAuthenticationToken> {

    private final val jwtGrantedAuthoritiesConvertor: JwtGrantedAuthoritiesConverter = JwtGrantedAuthoritiesConverter()

    @Value("\${jwt.auth.converter.principle-attribute}")
    private lateinit var principleAttribute: String

    @Value("\${jwt.auth.converter.resource-id}")
    private lateinit var resourceId: String

    override fun convert(@NonNull jwt: Jwt): AbstractAuthenticationToken? {
        val authorities: Collection<GrantedAuthority> = Stream.concat(
            jwtGrantedAuthoritiesConvertor.convert(jwt)!!.stream(),
            extractResourceRoles(jwt).stream()
        )
            .collect(Collectors.toSet())

        return JwtAuthenticationToken(jwt, authorities, getPrincipleClaimName(jwt))
    }

    private fun getPrincipleClaimName(jwt: Jwt): String {
        return jwt.getClaim(principleAttribute)
    }

    private fun extractResourceRoles(jwt: Jwt): Collection<GrantedAuthority> {
        val resourceAccess = jwt.getClaim<Any>("resource_access") as? Map<*, *> ?: return emptySet()
        val resource = resourceAccess[resourceId] as? Map<*, *> ?: return emptySet()
        val resourceRoles = resource["roles"] as? Collection<*> ?: return emptySet()

        return resourceRoles
            .stream()
            .map { SimpleGrantedAuthority("ROLE_$it") }
            .collect(Collectors.toSet())
    }

}