import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IProfileConfValue } from 'app/shared/model/profile-conf-value.model';

@Component({
    selector: 'jhi-profile-conf-value-detail',
    templateUrl: './profile-conf-value-detail.component.html'
})
export class ProfileConfValueDetailComponent implements OnInit {
    profileConfValue: IProfileConfValue;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ profileConfValue }) => {
            this.profileConfValue = profileConfValue;
        });
    }

    previousState() {
        window.history.back();
    }
}
