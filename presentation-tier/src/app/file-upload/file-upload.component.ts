import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import {FormGroup,FormControl} from '@angular/forms';
import { FileUploadService } from '../file-upload.service';

@Component({
  selector: 'app-file-upload',
  templateUrl: './file-upload.component.html',
  styleUrls: ['./file-upload.component.css']
})
export class FileUploadComponent implements OnInit {

  fileContent: any =[];
  data:any;
  constructor(private fileUpload:FileUploadService) { }
 
  ngOnInit(): void {

  }

  public onChange(fileList: File): void {
    let file = fileList[0];
    let fileReader: FileReader = new FileReader();
    let self = this;    
    fileReader.onload = () => {
      this.data = fileReader.result;
      self.fileContent = JSON.parse(this.data); 
      this.fileUpload.sendFileData(self.fileContent).subscribe(response =>{
        console.log(response);
      });
    }
    fileReader.readAsText(file);
  }
  
}


