package com.sjp.data.repository.base.datasource.exception

import java.io.IOException

class NetworkConnectionErrorException(message: String?) :
    IOException(message)