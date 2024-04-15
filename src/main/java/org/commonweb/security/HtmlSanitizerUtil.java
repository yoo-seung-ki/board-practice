package org.commonweb.security;

import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;
import org.springframework.stereotype.Component;

@Component
public class HtmlSanitizerUtil implements HtmlSanitizer{

    private final PolicyFactory policy = Sanitizers.FORMATTING.and(Sanitizers.LINKS);

    @Override
    public String sanitize(String input) {
        return policy.sanitize(input);
    }

}
