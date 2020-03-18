import { Injectable } from '@angular/core';
import { HttpClient,HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type': 'application/json',
  })
}
@Injectable({
  providedIn: 'root'
})
export class FileUploadService {

  constructor(private http: HttpClient) { }

  statementsUrl: string = "/statementprocessor-service/statement/process";

sendFileData(fileContent): Observable<any> {
  return this.http.post<any>(this.statementsUrl,fileContent,httpOptions);
}
}
