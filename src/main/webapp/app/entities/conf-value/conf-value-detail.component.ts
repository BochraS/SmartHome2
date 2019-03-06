import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IConfValue } from 'app/shared/model/conf-value.model';

@Component({
    selector: 'jhi-conf-value-detail',
    templateUrl: './conf-value-detail.component.html'
})
export class ConfValueDetailComponent implements OnInit {
    confValue: IConfValue;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ confValue }) => {
            this.confValue = confValue;
        });
    }

    previousState() {
        window.history.back();
    }
}
