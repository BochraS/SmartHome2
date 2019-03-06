import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISmartObject } from 'app/shared/model/smart-object.model';

@Component({
    selector: 'jhi-smart-object-detail',
    templateUrl: './smart-object-detail.component.html'
})
export class SmartObjectDetailComponent implements OnInit {
    smartObject: ISmartObject;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ smartObject }) => {
            this.smartObject = smartObject;
        });
    }

    previousState() {
        window.history.back();
    }
}
