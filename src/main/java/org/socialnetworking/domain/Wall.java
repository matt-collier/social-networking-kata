package org.socialnetworking.domain;

import java.util.List;

public record Wall(List<Posted> postedList) implements Response {

}
