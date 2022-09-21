package pl.edu.pg.eti.data.network

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Loading<T>(data: T? = null) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)

    override fun toString(): String {
        return "Resource - ${this::class.simpleName}(data=$data, message=$message)"
    }


}