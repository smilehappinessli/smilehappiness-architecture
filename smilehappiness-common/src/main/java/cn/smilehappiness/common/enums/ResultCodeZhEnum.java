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
public enum ResultCodeZhEnum {

    CONTINUE("100", "Continue", "Please continue to send the rest of the request "),
    SWITCHING_PROTOCOLS("101", "Switching Protocols", "Protocol switching "),
    PROCESSING("102", "Processing", "Request will continue "),
    CHECKPOINT("103", "Checkpoint", "Can be preloaded "),
    OK("200", "OK", "The request has been processed successfully "),
    CREATED("201", "Created", "The request has been processed successfully and the resource has been created "),
    ACCEPTED("202", "Accepted", "The request has been accepted, waiting for execution "),
    NON_AUTHORITATIVE_INFORMATION("203", "Non-Authoritative Information", "The request has been processed successfully, but the information is not original "),
    NO_CONTENT("204", "No Content", "The request has been processed successfully. No content needs to be returned "),
    RESET_CONTENT("205", "Reset Content", "The request has been processed successfully, please reset the view "),
    PARTIAL_CONTENT("206", "Partial Content", "Some Get requests have been processed successfully "),
    MULTI_STATUS("207", "Multi-Status", "The request has been processed successfully, and the XML message body will be returned "),
    ALREADY_REPORTED("208", "Already Reported", "The request has been processed successfully. The binding members of a DAV are enumerated by the previous request and are not included again "),
    IM_USED("226", "IM Used", "The request has been successfully processed and will respond to one or more instances "),
    MULTIPLE_CHOICES("300", "Multiple Choices", "Provide alternative feedback "),
    MOVED_PERMANENTLY("301", "Moved Permanently", "The requested resource has been permanently transferred "),
    FOUND("302", "Found", "Please resend the request "),
    SEE_OTHER("303", "See Other", "Please request another one in Get mode URI"),
    NOT_MODIFIED("304", "Not Modified", "Resource unchanged "),
    USE_PROXY("305", "Use Proxy", "Please access through the proxy in the Location domain "),
    TEMPORARY_REDIRECT("307", "Temporary Redirect", "The requested resource temporarily responds to the request from a different URI "),
    RESUME_INCOMPLETE("308", "Resume Incomplete", "The requested resource has been permanently transferred "),
    BAD_REQUEST("400", "Bad Request", "Request error, please correct the request "),
    UNAUTHORIZED("401", "Unauthorized", "Not authorized or the authorization has expired "),
    PAYMENT_REQUIRED("402", "Payment Required", "Reservation status "),
    FORBIDDEN("403", "Forbidden", "The request was understood but refused to be executed "),
    NOT_FOUND("404", "Not Found", "Resource not found "),
    METHOD_NOT_ALLOWED("405", "Method Not Allowed", "The request method is not allowed to be executed "),
    NOT_ACCEPTABLE("406", "Not Acceptable", "The requested resource does not meet the requester's requirements "),
    PROXY_AUTHENTICATION_REQUIRED("407", "Proxy Authentication Required", "Please authenticate through proxy "),
    REQUEST_TIMEOUT("408", "Request Timeout", "request timeout "),
    CONFLICT("409", "Conflict", "Request conflict "),
    GONE("410", "Gone", "The requested resource is not available "),
    LENGTH_REQUIRED("411", "Length Required", "Content-LengthUndefined "),
    PRECONDITION_FAILED("412", "Precondition Failed", "Prerequisites not met "),
    REQUEST_ENTITY_TOO_LARGE("413", "Request Entity Too Large", "The entity sent by the request is too large "),
    REQUEST_URI_TOO_LONG("414", "Request-URI Too Long", "The requested URI is too long "),
    UNSUPPORTED_MEDIA_TYPE("415", "Unsupported Media Type", "The entity type sent by the request is not supported "),
    REQUESTED_RANGE_NOT_SATISFIABLE("416", "Requested range not satisfiable", "RangeThe specified range is inconsistent with the current resource available range "),
    EXPECTATION_FAILED("417", "Expectation Failed", "The expected content specified in the request header Expect cannot be satisfied by the server "),
    UNPROCESSABLE_ENTITY("422", "Unprocessable Entity", "The request format is correct, but cannot respond because it contains semantic errors "),
    LOCKED("423", "Locked", "The current resource is locked "),
    FAILED_DEPENDENCY("424", "Failed Dependency", "The current request failed due to an error in the previous request "),
    UPGRADE_REQUIRED("426", "Upgrade Required", "The client needs to switch to TLS1.0"),
    PRECONDITION_REQUIRED("428", "Precondition Required", "The request needs to provide preconditions "),
    TOO_MANY_REQUESTS("429", "Too Many Requests", "Too many requests "),
    REQUEST_HEADER_FIELDS_TOO_LARGE("431", "Request Header Fields Too Large", "The request header is too large, and the request is rejected "),
    INTERNAL_SERVER_ERROR("500", "Internal Server Error", "Server internal exception, please try again later "),
    NOT_IMPLEMENTED("501", "Not Implemented", "The server does not support some of the functions currently requested "),
    BAD_GATEWAY("502", "Bad Gateway", "Invalid gateway service response "),
    SERVICE_UNAVAILABLE("503", "Service Unavailable", "Server maintenance or overload, denial of service "),
    GATEWAY_TIMEOUT("504", "Gateway Timeout", "Upstream server timeout "),
    HTTP_VERSION_NOT_SUPPORTED("505", "HTTP Version not supported", "HTTP Version Not Supported  "),
    VARIANT_ALSO_NEGOTIATES("506", "Variant Also Negotiates", "Server internal configuration error "),
    INSUFFICIENT_STORAGE("507", "Insufficient Storage", "The server could not complete the content required for the storage request "),
    LOOP_DETECTED("508", "Loop Detected", "The server found a loop when processing the request "),
    BANDWIDTH_LIMIT_EXCEEDED("509", "Bandwidth Limit Exceeded", "The server has reached the bandwidth limit "),
    NOT_EXTENDED("510", "Not Extended", "The strategy required to obtain resources is not met "),
    NETWORK_AUTHENTICATION_REQUIRED("511", "Network Authentication Required", "Network authorization is required ");

    private final String code;
    private final String enMessage;
    private final String message;

    ResultCodeZhEnum(String code, String enMessage, String message) {
        this.code = code;
        this.enMessage = enMessage;
        this.message = message;
    }

}
