package wrzecond

/**
 * Class used to return API requests to client
 * created from TjvEntity
 */
interface IReadDTO {
    val id: Int
    val displayName: String
}

/**
 * Class used in API requests from client
 * converted to TjvEntity
 */
interface ICreateDTO

/**
 * Class used in API requests from client
 * being merged with TjvEntity
 */
interface IUpdateDTO