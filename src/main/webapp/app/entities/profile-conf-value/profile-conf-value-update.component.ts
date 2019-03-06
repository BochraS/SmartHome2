import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IProfileConfValue } from 'app/shared/model/profile-conf-value.model';
import { ProfileConfValueService } from './profile-conf-value.service';

@Component({
    selector: 'jhi-profile-conf-value-update',
    templateUrl: './profile-conf-value-update.component.html'
})
export class ProfileConfValueUpdateComponent implements OnInit {
    profileConfValue: IProfileConfValue;
    isSaving: boolean;

    constructor(protected profileConfValueService: ProfileConfValueService, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ profileConfValue }) => {
            this.profileConfValue = profileConfValue;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.profileConfValue.id !== undefined) {
            this.subscribeToSaveResponse(this.profileConfValueService.update(this.profileConfValue));
        } else {
            this.subscribeToSaveResponse(this.profileConfValueService.create(this.profileConfValue));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IProfileConfValue>>) {
        result.subscribe((res: HttpResponse<IProfileConfValue>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }
}
