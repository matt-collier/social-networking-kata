package org.socialnetworking.domain;

import java.util.List;

public record Timeline(List<String> messages) implements Response {

}
