
import {
  HttpEvent, HttpHandler, HttpInterceptor, HttpRequest
} from "@angular/common/http";
import { Injectable } from "@angular/core";

import { MessageService } from "primeng/api";
import { Observable, throwError } from "rxjs";
import { catchError } from "rxjs/operators";
import { showMessage } from "src/app/utils/showMessage";
import { LoadingService } from "../services/loading.service";

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {
  constructor(private _loading: LoadingService,
    private messageService: MessageService
  ) { }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(req).pipe(
      catchError((error: any) => {
        this.tratarMensagemRetornoErro(error);
        this._loading.setLoading(false, req.url);
        return throwError(error);
      })
    );
  }

  tratarMensagemRetornoErro(ex: any) {

    if (ex.status === 400) {
      this.emitirMensagem(ex.error.detail, 'warn');
    } else if (ex.status === 404) {
      this.emitirMensagem('Não encontrado', 'info');
      return;
    } else if (ex.status === 500) {
      this.emitirMensagem(
        'Houve um erro interno na aplicação, por favor tente novamente mais tarde ou consulte o suporte',
        'error');
    } else {
      this.emitirMensagem('Serviço indisponível', 'error');
      return;
    }
  }

  emitirMensagem(msg: any, titulo: string) {
    showMessage(this.messageService, {
      severity: titulo,
      detail: msg
    })
  }


}
