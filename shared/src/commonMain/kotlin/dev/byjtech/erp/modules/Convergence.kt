package dev.byjtech.erp.modules

//una carpeta por cada modulo, debe contener una carpeta para sus DTO y un archivo <Modulo>Definition.kt
//donde se definen los permisos y la infomacion basica del modulo y ereda de ModuleDefinition

//luego se agrega al modulo koin del shared para que pueda ser injectado en el frontend y bakend (al crear el moduleInitializer)

//seguir la forma del CoreDefinition.kt