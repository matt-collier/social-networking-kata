package org.socialnetworking.domain;

public sealed interface Response permits Timeline, VoidResponse, Wall {
}
