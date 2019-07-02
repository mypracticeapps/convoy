import { Component } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import {GitRepo} from './model/git-repo';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'frontend';

  constructor(private http: HttpClient){
    this.ngOnInit();
  }

  ngOnInit(){
    this.http.get<GitRepo[]>("http://localhost:8080/api/v1/repos").subscribe((repos)=>console.log);
  }
}
