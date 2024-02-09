package ar.edu.unlam.tallerweb1.domain.exceptions

class UserNotExists(name: String) : BusinessException("$name do not exist")
