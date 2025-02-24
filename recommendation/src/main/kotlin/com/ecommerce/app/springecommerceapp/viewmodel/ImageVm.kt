package com.ecommerce.app.springecommerceapp.viewmodel

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class ImageVm(val id: Long, val url: String)
