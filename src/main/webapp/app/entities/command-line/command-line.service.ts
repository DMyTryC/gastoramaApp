import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ICommandLine } from 'app/shared/model/command-line.model';

type EntityResponseType = HttpResponse<ICommandLine>;
type EntityArrayResponseType = HttpResponse<ICommandLine[]>;

@Injectable({ providedIn: 'root' })
export class CommandLineService {
  private resourceUrl = SERVER_API_URL + 'api/command-lines';
  private resourceSearchUrl = SERVER_API_URL + 'api/_search/command-lines';

  constructor(private http: HttpClient) {}

  create(commandLine: ICommandLine): Observable<EntityResponseType> {
    return this.http.post<ICommandLine>(this.resourceUrl, commandLine, {
      observe: 'response'
    });
  }

  update(commandLine: ICommandLine): Observable<EntityResponseType> {
    return this.http.put<ICommandLine>(this.resourceUrl, commandLine, {
      observe: 'response'
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICommandLine>(`${this.resourceUrl}/${id}`, {
      observe: 'response'
    });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICommandLine[]>(this.resourceUrl, {
      params: options,
      observe: 'response'
    });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, {
      observe: 'response'
    });
  }

  search(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICommandLine[]>(this.resourceSearchUrl, {
      params: options,
      observe: 'response'
    });
  }
}
