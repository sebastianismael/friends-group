package ar.edu.unlam.tallerweb1.domain.exceptions

class UserWithoutFriendsGroup(name: String) : BusinessException("$name doesn't have a friends group")
