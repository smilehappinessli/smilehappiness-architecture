package cn.smilehappiness.common.enums;

import lombok.Getter;

/**
 * <p>
 * HttpBasic status code enumeration class 
 * <p/>
 *
 * @author
 * @Date 2021/10/15 11:38
 */
@Getter
@SuppressWarnings("all")
public enum ResultCodeEnum {

    // 1xx Informational

    /**
     * {@code 100 Continue}.
     */
    CONTINUE("100", "Continue", "Melanjutkan"),
    /**
     * {@code 101 Switching Protocols}.
     */
    SWITCHING_PROTOCOLS("101", "Switching Protocols", "Beralih Protokol"),
    /**
     * {@code 102 Processing}.
     */
    PROCESSING("102", "Processing", "Pengolahan"),
    /**
     * {@code 103 Checkpoint}.
     */
    CHECKPOINT("103", "Checkpoint", "Pos pemeriksaan"),

    // 2xx Success

    /**
     * {@code 200 OK}.
     */
    OK("200", "OK", "Oke"),
    /**
     * {@code 201 Created}.
     */
    CREATED("201", "Created", "Dibuat"),
    /**
     * {@code 202 Accepted}.
     */
    ACCEPTED("202", "Accepted", "Diterima"),
    /**
     * {@code 203 Non-Authoritative Information}.
     */
    NON_AUTHORITATIVE_INFORMATION("203", "Non-Authoritative Information", "Informasi Non-Otoritatif"),
    /**
     * {@code 204 No Content}.
     */
    NO_CONTENT("204", "No Content", "Tidak ada isi"),
    /**
     * {@code 205 Reset Content}.
     */
    RESET_CONTENT("205", "Reset Content", "Setel Ulang Konten"),
    /**
     * {@code 206 Partial Content}.
     */
    PARTIAL_CONTENT("206", "Partial Content", "Konten Sebagian"),
    /**
     * {@code 207 Multi-Status}.
     */
    MULTI_STATUS("207", "Multi-Status", "Multi-Status"),
    /**
     * {@code 208 Already Reported}.
     */
    ALREADY_REPORTED("208", "Already Reported", "Sudah Dilaporkan"),
    /**
     * {@code 226 IM Used}.
     */
    IM_USED("226", "IM Used", "IM Digunakan"),

    // 3xx Redirection

    /**
     * {@code 300 Multiple Choices}.
     */
    MULTIPLE_CHOICES("300", "Multiple Choices", "Pilihan ganda"),
    /**
     * {@code 301 Moved Permanently}.
     */
    MOVED_PERMANENTLY("301", "Moved Permanently", "Dipindahkan Secara Permanen"),
    /**
     * {@code 302 Found}.
     */
    FOUND("302", "Found", "Ditemukan"),
    /**
     * {@code 302 Moved Temporarily}.
     */
    @Deprecated
    MOVED_TEMPORARILY("302", "Moved Temporarily", "pindah sementara"),
    /**
     * {@code 303 See Other}.
     */
    SEE_OTHER("303", "See Other", "Lihat Lainnya"),
    /**
     * {@code 304 Not Modified}.
     */
    NOT_MODIFIED("304", "Not Modified", "Tidak dimodifikasi"),
    /**
     * {@code 305 Use Proxy}.
     */
    @Deprecated
    USE_PROXY("305", "Use Proxy", "Gunakan proxy"),
    /**
     * {@code 307 Temporary Redirect}.
     */
    TEMPORARY_REDIRECT("307", "Temporary Redirect", "Pengalihan Sementara"),
    /**
     * {@code 308 Permanent Redirect}.
     */
    PERMANENT_REDIRECT("308", "Permanent Redirect", "Pengalihan Permanen"),

    // --- 4xx Client Error ---

    /**
     * {@code 400 Bad Request}.
     */
    BAD_REQUEST("400", "Bad Request", "Permintaan yang buruk"),
    /**
     * {@code 401 Unauthorized}.
     */
    UNAUTHORIZED("401", "Unauthorized", "tidak sah"),
    /**
     * {@code 402 Payment Required}.
     */
    PAYMENT_REQUIRED("402", "Payment Required", "Pembayaran Diperlukan"),
    /**
     * {@code 403 Forbidden}.
     */
    FORBIDDEN("403", "Forbidden", "Terlarang"),
    /**
     * {@code 404 Not Found}.
     */
    NOT_FOUND("404", "Not Found", "Tidak ditemukan"),
    /**
     * {@code 405 Method Not Allowed}.
     */
    METHOD_NOT_ALLOWED("405", "Method Not Allowed", "Metode Tidak Diizinkan"),
    /**
     * {@code 406 Not Acceptable}.
     */
    NOT_ACCEPTABLE("406", "Not Acceptable", "Tidak dapat diterima"),
    /**
     * {@code 407 Proxy Authentication Required}.
     */
    PROXY_AUTHENTICATION_REQUIRED("407", "Proxy Authentication Required", "Otentikasi Proksi Diperlukan"),
    /**
     * {@code 408 Request Timeout}.
     */
    REQUEST_TIMEOUT("408", "Request Timeout", "Minta Waktu Habis"),
    /**
     * {@code 409 Conflict}.
     */
    CONFLICT("409", "Conflict", "Konflik"),
    /**
     * {@code 410 Gone}.
     */
    GONE("410", "Gone", "Hilang"),
    /**
     * {@code 411 Length Required}.
     */
    LENGTH_REQUIRED("411", "Length Required", "Panjang yang Diperlukan"),
    /**
     * {@code 412 Precondition failed}.
     */
    PRECONDITION_FAILED("412", "Precondition Failed", "Prasyarat Gagal"),
    /**
     * {@code 413 Payload Too Large}.
     */
    PAYLOAD_TOO_LARGE("413", "Payload Too Large", "Payload Terlalu Besar"),
    /**
     * {@code 413 Request Entity Too Large}.
     */
    @Deprecated
    REQUEST_ENTITY_TOO_LARGE("413", "Request Entity Too Large", "Entitas Permintaan Terlalu Besar"),
    /**
     * {@code 414 URI Too Long}.
     */
    URI_TOO_LONG("414", "URI Too Long", "URI Terlalu Panjang"),
    /**
     * {@code 414 Request-URI Too Long}.
     */
    @Deprecated
    REQUEST_URI_TOO_LONG("414", "Request-URI Too Long", "Permintaan-URI Terlalu Panjang"),
    /**
     * {@code 415 Unsupported Media Type}.
     */
    UNSUPPORTED_MEDIA_TYPE("415", "Unsupported Media Type", "Jenis Media yang Tidak Didukung"),
    /**
     * {@code 416 Requested Range Not Satisfiable}.
     */
    REQUESTED_RANGE_NOT_SATISFIABLE("416", "Requested range not satisfiable", "Rentang yang diminta tidak memuaskan"),
    /**
     * {@code 417 Expectation Failed}.
     */
    EXPECTATION_FAILED("417", "Expectation Failed", "Harapan Gagal"),
    /**
     * {@code 418 I'm a teapot}.
     */
    I_AM_A_TEAPOT("418", "I'm a teapot", "aku teko"),
    /**
     * @deprecated
     */
    @Deprecated
    INSUFFICIENT_SPACE_ON_RESOURCE("419", "Insufficient Space On Resource", "Ruang Sumber Daya Tidak Cukup"),
    /**
     * @deprecated
     */
    @Deprecated
    METHOD_FAILURE("420", "Method Failure", "Kegagalan Metode"),
    /**
     * @deprecated
     */
    @Deprecated
    DESTINATION_LOCKED("421", "Destination Locked", "Tujuan Terkunci"),
    /**
     * {@code 422 Unprocessable Entity}.
     */
    UNPROCESSABLE_ENTITY("422", "Unprocessable Entity", "Entitas yang Tidak Dapat Diproses"),
    /**
     * {@code 423 Locked}.
     */
    LOCKED("423", "Locked", "Terkunci"),
    /**
     * {@code 424 Failed Dependency}.
     */
    FAILED_DEPENDENCY("424", "Failed Dependency", "Ketergantungan yang Gagal"),
    /**
     * {@code 425 Too Early}.
     */
    TOO_EARLY("425", "Too Early", "Terlalu dini"),
    /**
     * {@code 426 Upgrade Required}.
     */
    UPGRADE_REQUIRED("426", "Upgrade Required", "Diperlukan Peningkatan"),
    /**
     * {@code 428 Precondition Required}.
     */
    PRECONDITION_REQUIRED("428", "Precondition Required", "Prasyarat Diperlukan"),
    /**
     * {@code 429 Too Many Requests}.
     */
    TOO_MANY_REQUESTS("429", "Too Many Requests", "Terlalu Banyak Permintaan"),
    /**
     * {@code 431 Request Header Fields Too Large}.
     */
    REQUEST_HEADER_FIELDS_TOO_LARGE("431", "Request Header Fields Too Large", "Minta Bidang Tajuk Terlalu Besar"),
    /**
     * {@code 451 Unavailable For Legal Reasons}.
     */
    UNAVAILABLE_FOR_LEGAL_REASONS("451", "Unavailable For Legal Reasons", "Tidak Tersedia Karena Alasan Hukum"),

    // --- 5xx Server Error ---

    /**
     * {@code 500 Internal Server Error}.
     */
    INTERNAL_SERVER_ERROR("500", "Internal Server Error", "Kesalahan server dari dalam"),
    /**
     * {@code 501 Not Implemented}.
     */
    NOT_IMPLEMENTED("501", "Not Implemented", "Tidak Diimplementasikan"),
    /**
     * {@code 502 Bad Gateway}.
     */
    BAD_GATEWAY("502", "Bad Gateway", "Gerbang Buruk"),
    /**
     * {@code 503 Service Unavailable}.
     */
    SERVICE_UNAVAILABLE("503", "Service Unavailable", "Layanan tidak tersedia"),
    /**
     * {@code 504 Gateway Timeout}.
     */
    GATEWAY_TIMEOUT("504", "Gateway Timeout", "Batas Waktu Gerbang"),
    /**
     * {@code 505 HTTP Version Not Supported}.
     */
    HTTP_VERSION_NOT_SUPPORTED("505", "HTTP Version not supported", "Versi HTTP tidak didukung"),
    /**
     * {@code 506 Variant Also Negotiates}
     */
    VARIANT_ALSO_NEGOTIATES("506", "Variant Also Negotiates", "Varian Juga Negosiasi"),
    /**
     * {@code 507 Insufficient Storage}
     */
    INSUFFICIENT_STORAGE("507", "Insufficient Storage", "Penyimpanan tidak cukup"),
    /**
     * {@code 508 Loop Detected}
     */
    LOOP_DETECTED("508", "Loop Detected", "Lingkaran Terdeteksi"),
    /**
     * {@code 509 Bandwidth Limit Exceeded}
     */
    BANDWIDTH_LIMIT_EXCEEDED("509", "Bandwidth Limit Exceeded", "Batas Bandwidth Terlampaui"),
    /**
     * {@code 510 Not Extended}
     */
    NOT_EXTENDED("510", "Not Extended", "Tidak Diperpanjang"),
    /**
     * {@code 511 Network Authentication Required}.
     */
    NETWORK_AUTHENTICATION_REQUIRED("511", "Network Authentication Required", "Diperlukan Otentikasi Jaringan");

    private final String code;
    private final String enMessage;
    private final String message;

    ResultCodeEnum(String code, String enMessage, String message) {
        this.code = code;
        this.enMessage = enMessage;
        this.message = message;
    }

}
