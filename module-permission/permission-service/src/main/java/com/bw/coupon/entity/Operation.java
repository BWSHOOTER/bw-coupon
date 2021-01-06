package com.bw.coupon.entity;

import com.bw.coupon.vo.OperationRegisterRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "operation")
public class Operation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "op_des", nullable = false)
    private String opDes;

    @Column(name = "op_mode", nullable = false)
    private String opMode;

    @Column(name = "request_service", nullable = false)
    private String requestService;

    @Column(name = "request_path", nullable = false)
    private String requestPath;

    @Column(name = "http_method", nullable = false)
    private String httpMethod;

    public Operation(String opDes,
                     String opMode,
                     String requestService,
                     String requestPath,
                     String httpMethod){
        this.opDes = opDes;
        this.opMode = opMode;
        this.requestService = requestService;
        this.requestPath = requestPath;
        this.httpMethod = httpMethod;
    }

    public Operation(OperationRegisterRequest.OperationInfo info){
        this.opDes = info.getOperationDes();
        this.opMode = info.getOperationMode();
        this.requestService = info.getRequestService();
        this.requestPath = info.getRequestPath();
        this.httpMethod = info.getHttpMethod();
    }
}
