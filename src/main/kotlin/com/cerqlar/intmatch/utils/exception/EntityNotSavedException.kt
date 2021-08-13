package com.cerqlar.intmatch.utils.exception

import java.lang.Exception

/**
 * Created by chinnku on Aug, 2021
 * EntityNotSavedException
 */
class EntityNotSavedException(override val message:String?):Exception(message)